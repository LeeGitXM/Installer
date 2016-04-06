
'''
      Output is the change in the flash drum calcium targets to get the product calcium laboratory value back on target.   
'''

import system

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")
    textRecommendation=""
    recommendations=[]

    tagName = "[%s]LabData/RLA3/CA-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        log.error("In CA.calculate() - the target value (SP) is bad (%s) %s" % (tagName, str(sp.quality)))
        return textRecommendation,recommendations
    sp = sp.value

    tagName = "[%s]LabData/RLA3/CA-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        log.error("In CA.calculate() - PV is bad (%s) %s" % (tagName, str(pv.quality)))
        return textRecommendation,recommendations
    pv=pv.value
    
    tagName = "[%s]DiagnosticToolkit/CSTR/VFS000ME/value" % (provider)
    d20 = system.tag.read(tagName)
    if not (d20.quality.isGood()):
        log.error("In CA.calculate() - D20 service status is bad (%s) %s" % (tagName, str(d20.quality)))
        return textRecommendation,recommendations
    d20=round(d20.value)

    tagName = "[%s]DiagnosticToolkit/CSTR/VFS100ME/value" % (provider)
    d20a = system.tag.read(tagName)
    if not (d20a.quality.isGood()):
        log.error("In CA.calculate() - D20A service status is bad (%s) %s" % (tagName, str(d20a.quality)))
        return textRecommendation,recommendations
    d20a=round(d20a.value)
    
    tagName = "[%s]DiagnosticToolkit/CSTR/VFS200ME/value" % (provider)
    d20b = system.tag.read(tagName)
    if not (d20b.quality.isGood()):
        log.error("In CA.calculate() - D20B service status is bad (%s) %s" % (tagName, str(d20b.quality)))
        return textRecommendation,recommendations
    d20b=round(d20b.value)

    log.trace("In CA.calculate(): PV = %s, SP = %s, D20 = %s, D20a = %s, and D20b = %s" % (str(pv), str(sp), str(d20), str(d20a), str(d20b)))

    error = (pv - sp)

    # Delta Calcium 1
    if abs(d20 - 1.0) < 0.1:
        recommendations.append({"QuantOutput":"VFA009Z_TARGET", "Value": -0.25 * error})

    # Delta Calcium 2
    if abs(d20a - 1.0) < 0.1:
        recommendations.append({"QuantOutput":"VFA109Z_TARGET", "Value": -0.25 * error})

    # Delta Calcium 3
    if abs(d20b - 1.0) < 0.1:
        recommendations.append({"QuantOutput":"VFA209Z_TARGET", "Value": -0.25 * error})

    return textRecommendation,recommendations
