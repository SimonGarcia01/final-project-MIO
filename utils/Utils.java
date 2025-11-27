public class Utils {


    public static void main(String[] args) {

        // Berlin
        double lat1 = 52.5200;
        double lon1 = 13.4050;

        // Paris
        double lat2 = 48.8566; 
        double lon2 = 2.3522;

        double distance = distanceKm(lat1, lon1, lat2, lon2);

        System.out.println(distance + " km");
    }



    public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0; //Earths radius in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;  // distance in kms
    }
}