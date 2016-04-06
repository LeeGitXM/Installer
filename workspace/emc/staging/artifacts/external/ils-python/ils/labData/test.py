'''
Created on Jul 7, 2015

@author: Pete
'''
import system

def hdaRead():
    print "In ils.labData.test.hdaRead()"
    import ils.common.util as util
    endDate = util.getDate()
    from java.util import Calendar
    cal = Calendar.getInstance()
    
    cal.setTime(endDate)
    cal.add(Calendar.HOUR, -8)
    startDate = cal.getTime()
    
    hdaInterface="PHD-HDA"
    itemIds=['rv.r1_c2.lab', 'rv.r1_c9.lab']
    maxValues=0
    boundingValues=False
    
    print "Reading %s, Max Values: %i, Bounding Values: %s " % (str(itemIds), maxValues, str(boundingValues))
    retVals=system.opchda.readRaw(hdaInterface, itemIds, startDate, endDate, maxValues, boundingValues)
    print "...back from HDA read, read %i values!" % (len(retVals))
    
    i = 0
    for itemId in itemIds:
        print "Tag: ", itemId
        valueList=retVals[i]
        for qv in valueList:
            rawValue=qv.value
            sampleTime=qv.timestamp
            quality=qv.quality
            print "    ", rawValue, sampleTime, quality
        i = i + 1
        

    
    print "-----------------"
    maxValues=1
    boundingValues=False
    print "Reading %s, Max Values: %i, Bounding Values: %s " % (str(itemIds), maxValues, str(boundingValues))
    retVals=system.opchda.readRaw(hdaInterface, itemIds, startDate, endDate, maxValues, boundingValues)
    print "...back from HDA read, read %i values!" % (len(retVals))
    
    i = 0
    for itemId in itemIds:
        print "Tag: ", itemId
        valueList=retVals[i]
        for qv in valueList:
            rawValue=qv.value
            sampleTime=qv.timestamp
            quality=qv.quality
            print "    ", rawValue, sampleTime, quality
        i = i + 1

    
    print "-----------------"
    maxValues=1
    boundingValues=True
    print "Reading %s, Max Values: %i, Bounding Values: %s " % (str(itemIds), maxValues, str(boundingValues))
    retVals=system.opchda.readRaw(hdaInterface, itemIds, endDate, endDate, maxValues, boundingValues)
    print "...back from HDA read, read %i values!" % (len(retVals))
    
    i = 0
    for itemId in itemIds:
        print "Tag: ", itemId
        valueList=retVals[i]
        for qv in valueList:
            rawValue=qv.value
            sampleTime=qv.timestamp
            quality=qv.quality
            print "    ", rawValue, sampleTime, quality
        i = i + 1
       
    print "All Done!"
    