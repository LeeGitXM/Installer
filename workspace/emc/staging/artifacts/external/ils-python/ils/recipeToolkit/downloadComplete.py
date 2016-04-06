'''
Created on Sep 10, 2014

@author: Pete
'''
import system
import ils.recipeToolkit.update as update
import com.inductiveautomation.ignition.common.util.LogUtil as LogUtil
log = LogUtil.getLogger("com.ils.recipeToolkit.download")

# This is called once it is deemed that the download is complete.  
# It summarizes the results of the download.
def downloadComplete(rootContainer):
    log.trace("In downloadComplete()...")
    logId = rootContainer.getPropertyValue("logId")
    grade = rootContainer.getPropertyValue("grade")
    version = rootContainer.getPropertyValue("version")
    recipeKey = rootContainer.getPropertyValue("familyName")
    downloadType = rootContainer.getPropertyValue("downloadType")
    table = rootContainer.getComponent("Power Table")

    ds = table.processedData
    
    status, downloads, successes, failures = downloadCompleteRunner(ds, logId, recipeKey, grade, version, "Manual", downloadType)

    from ils.recipeToolkit.common import setBackgroundColor
    if failures == 0:
        setBackgroundColor(rootContainer, "screenBackgroundColorSuccess")
    else:
        setBackgroundColor(rootContainer, "screenBackgroundColorFail")

    # Update the status of the rootContainer so the user can do a refresh
    rootContainer.status = status

#
def downloadCompleteRunner(ds, logId, recipeKey, grade, version, automatedOrManual, gradeChangeOrMidRun, database=""):
    import string
    log.trace("In downloadCompleteRunner()...")
    log.info("Download complete...")

    pds = system.dataset.toPyDataSet(ds)

    downloads = 0
    successes = 0
    failures = 0
    for record in pds:
        download = record["Download"]
        downloadType = record["Download Type"]
        downloadStatus = string.upper(record["Download Status"])

        if download:
            downloads = downloads + 1

            if downloadStatus == 'SUCCESS':
                successes = successes + 1
            else:
                failures = failures + 1

    log.info("...there were %i downloads (%i success, %i failed)" % (downloads, successes, failures))

    if failures == 0:
        status = "Success"
        print "Setting recipe family <%s> status to <Download Passed>" % (str(recipeKey))
        update.recipeFamilyStatus(recipeKey, 'Download Passed', database)
    else:
        status = "Failed"
        print "Setting recipe family <%s> status to <Download Failed>" % (str(recipeKey))
        update.recipeFamilyStatus(recipeKey, 'Download Failed', database)

    # Write a log book message
    txt = "%s Recipe download of %s - grade %s - version %s - type %s has completed.  %i writes confirmed.  %i writes NOT confirmed." % \
        (automatedOrManual, recipeKey, grade, str(version), gradeChangeOrMidRun, successes, failures)

    # Insert a message into the log book queue that we are starting a manual download
    from ils.queue.log import insert
    insert(txt, database)

    # Update the Master down table
    from ils.recipeToolkit.log import updateLogMaster
    updateLogMaster(logId, status, downloads, successes, failures, database)

    return status, downloads, successes, failures