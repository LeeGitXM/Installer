'''
Created on Aug 10, 2015

@author: Pete
'''
def fd1_1_1(application,finaldiagnosis, provider, database):
    print "In fd1_1_1"
    textRecommendation = "Close the valve because the flow is too great and needs to be minimized to reduce flooding in the control room."
    recommendations = []
    recommendations.append({"QuantOutput": "TESTQ1", "Value": 12.3})
    return textRecommendation, recommendations

def fd1_2_1(application,finaldiagnosis, provider, database):
    print "In fd1_2_1"
    textRecommendation = "The TESTFD1_2_1 will use data of gain = 1.2, 1.5, and 0.9, SP = 23.4."
    recommendations = []
    recommendations.append({"QuantOutput": "TESTQ1", "Value": 31.4})
    recommendations.append({"QuantOutput": "TESTQ2", "Value": 53.4})
    return textRecommendation, recommendations

def fd1_2_1b(application,finaldiagnosis, provider, database):
    print "In fd1_2_1b"
    textRecommendation = "The TESTFD1_2_1 will use data of gain = 1.2, 1.5, and 0.9, SP = 23.4."
    recommendations = []
    recommendations.append({"QuantOutput": "TESTQ1", "Value": 31.4})
    recommendations.append({"QuantOutput": "TESTQ2", "Value": 0.017})
    return textRecommendation, recommendations

def fd1_2_2(application,finaldiagnosis, provider, database):
    print "In fd1_2_2"
    textRecommendation = "Turn down the flame and open the window."
    recommendations = []
    recommendations.append({"QuantOutput": "TESTQ2", "Value": 5.4})
    recommendations.append({"QuantOutput": "TESTQ3", "Value": -20.4})
    return textRecommendation, recommendations

def fd1_2_3(application,finaldiagnosis, provider, database):
    print "In fd1_2_3 "
    textRecommendation = "Turn down the flame and open the window."
    recommendations = []
    recommendations.append({"QuantOutput": "TESTQ1", "Value": 6.8})
    recommendations.append({"QuantOutput": "TESTQ2", "Value": -12.3})
    recommendations.append({"QuantOutput": "TESTQ3", "Value": 15.8})
    return textRecommendation, recommendations

def fd1_2_3a(application,finaldiagnosis, provider, database):
    print "In fd1_2_3a - returning a 0.0 recommendation "
    textRecommendation = "Turn down the flame and open the window."
    recommendations = []
    recommendations.append({"QuantOutput": "TESTQ1", "Value": 6.8})
    recommendations.append({"QuantOutput": "TESTQ2", "Value": -12.3})
    recommendations.append({"QuantOutput": "TESTQ3", "Value": 0.0})
    return textRecommendation, recommendations

def fd1_2_3b(application,finaldiagnosis, provider, database):
    print "In fd1_2_3b - returning just 1 of the expected 3 recommendations "
    textRecommendation = "Turn down the flame and open the window."
    recommendations = []
    recommendations.append({"QuantOutput": "TESTQ1", "Value": 6.8})
    return textRecommendation, recommendations

def fd2_1_1(application,finaldiagnosis, provider, database):
    print "In fd2_1_1"
    textRecommendation = "Get the steak out."
    recommendations = []
    recommendations.append({"QuantOutput": "TEST_Q0_TC100", "Value": 19.88})
    return textRecommendation, recommendations

def lowViscosityHighFeed(application,fd, provider, database):
    print "Calculating the correction for Low Viscosity & High Feed"
    
    textRecommendation=""
    recommendations=[]
        
    recommendations.append({"QuantOutput": "QO1", "Value": 16.37})
    recommendations.append({"QuantOutput": "QO2", "Value": 5.61})
    recommendations.append({"QuantOutput": "QO3", "Value": 7.8})

    return textRecommendation,recommendations

def version():
    return "1.1"