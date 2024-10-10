import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    private static final String API_KEY = "bf5eecbaef9226be44f497a8";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("********");
            System.out.println("¡Sea bienvenido/a al Conversor de Moneda =]");
            System.out.println("1) Dólar =>> Peso argentino");
            System.out.println("2) Peso argentino =>> Dólar");
            System.out.println("3) Dólar =>> Real brasileño");
            System.out.println("4) Real brasileño =>> Dólar");
            System.out.println("5) Dólar =>> Peso colombiano");
            System.out.println("6) Peso colombiano =>> Dólar");
            System.out.println("7) Salir");
            System.out.print("Elija una opción válida: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    realizarConversion("USD", "ARS");
                    break;
                case 2:
                    realizarConversion("ARS", "USD");
                    break;
                case 3:
                    realizarConversion("USD", "BRL");
                    break;
                case 4:
                    realizarConversion("BRL", "USD");
                    break;
                case 5:
                    realizarConversion("USD", "COP");
                    break;
                case 6:
                    realizarConversion("COP", "USD");
                    break;
                case 7:
                    System.out.println("¡Gracias por usar el conversor!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (opcion != 7);

        scanner.close();
    }

    private static void realizarConversion(String deMoneda, String aMoneda) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a convertir de " + deMoneda + " a " + aMoneda + ": ");
        double cantidad = scanner.nextDouble();
        double resultado = convertirMoneda(cantidad, deMoneda, aMoneda);
        System.out.println(cantidad + " " + deMoneda + " son " + resultado + " " + aMoneda + ".");
    }

    public static double convertirMoneda(double cantidad, String deMoneda, String aMoneda) {
        try {
            URL url = new URL(API_URL + deMoneda);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonResponse = response.toString();
            double tasa = obtenerTasaDeConversion(jsonResponse, aMoneda);
            return cantidad * tasa;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static double obtenerTasaDeConversion(String jsonResponse, String aMoneda) {
        String[] partes = jsonResponse.split("\"" + aMoneda + "\":");
        if (partes.length > 1) {
            String[] tasaPartes = partes[1].split(",");
            return Double.parseDouble(tasaPartes[0]);
        } else {
            throw new IllegalArgumentException("La moneda " + aMoneda + " no fue encontrada.");
        }
    }
}
