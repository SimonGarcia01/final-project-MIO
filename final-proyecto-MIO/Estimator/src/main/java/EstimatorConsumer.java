import Demo.ArcUpdate;
import Demo.Data;

public class EstimatorConsumer {

    public ArcUpdate estimateArcUpdate(Data data) {
        // TODO: Lógica real de cálculo
        // Simulación para pruebas:
        ArcUpdate response = new ArcUpdate();
        response.averageSpeed = 0.0;
        response.stopMatrixId1 = 0;
        response.stopMatrixId2 = 0;
        response.bus = null;

        return response;
    }
}