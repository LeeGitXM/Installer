'''
  Gateway scope extension functions dealing with creation of diagrams.
'''
import system
import com.ils.blt.gateway.ControllerRequestHandler as ControllerRequestHandler

handler = ControllerRequestHandler.getInstance()

def create(diagramId):
    db   = handler.getDatabaseForUUID(diagramId)
    blocks = handler.listBlocksInDiagram(diagramId)
    for block in blocks:
        #print block.getClassName(),block.getName(),block.getIdString()
        pass
    
#