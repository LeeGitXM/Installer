# Copyright 2015 ILS Automation. All rights reserved.

'''
      Output is the change in the weight percent stabilizer to get the laboratory value back on target.  Called by final fiagnosis named STAB_PROBLEM_FOR_CSTR and STAB_PROB_FOR_CRX.   
'''

import system

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")
    textRecommendation=""
    recommendations=[]
    log.info("In CrxStab.calculate()")

    tagName = "[%s]LabData/RLA3/STAB-FILTERED-VALUE/filteredValue" % (provider)
    PV = system.tag.read(tagName)
    if not (PV.quality.isGood()):
        log.error("In CrxStab.calculate() - PV is bad (%s) %s" % (tagName, str(PV.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]LabData/RLA3/STAB-LAB-DATA-SQC/lowerSQCLimit" % (provider)
    PVLL = system.tag.read(tagName)
    if not (PVLL.quality.isGood()):
        log.error("In CrxStab.calculate() - PVLL is bad (%s) %s" % (tagName, str(PVLL.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]LabData/RLA3/STAB-LAB-DATA-SQC/target" % (provider)
    SP = system.tag.read(tagName)
    if not (SP.quality.isGood()):
        log.error("In CrxStab.calculate() - SP is bad (%s) %s" % (tagName, str(SP.quality)))
        return textRecommendation,recommendations

    log.trace("In CrxStab.calculate():  PV and SP are %s and %s" % (str(PV.value), str(SP.value)))

    pctchg = (SP.value - PVLL.value) * 100.0 / SP.value / 2.0
    error = (PV.value - SP.value)

    recommendations.append({"QuantOutput":"VMF223R1_TARGET_CRX", "Value": -pctchg * error / 0.01})

#    message.insert(application,"%s has updated %s output."%("CrxStab.calculate",lib.getBlockName(fd)),str(EMCConstants.INFORMATION),fd)
#   outputMessage.create(fd)
    return textRecommendation,recommendations
