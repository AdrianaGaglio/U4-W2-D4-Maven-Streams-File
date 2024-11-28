package it.epicode;

import it.epicode.entities.Customer;
import it.epicode.entities.Order;
import it.epicode.entities.Product;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Application {
    public static final String LOG_PRODOTTI_TXT = "log/prodotti.txt";

    public static void main(String[] args) throws IOException {

        // creo catalogo prodotti
        Product p1 = new Product(1L, "T-shirt uomo", "Men's Clothing", 25.0);
        Product p2 = new Product(2L, "Jeans uomo", "Men's Clothing", 50.0);
        Product p3 = new Product(3L, "Giacca uomo", "Men's Clothing", 120.0);

        Product p4 = new Product(4L, "Abito donna", "Women's Clothing", 80.0);
        Product p5 = new Product(5L, "Top donna", "Women's Clothing", 30.0);
        Product p6 = new Product(6L, "Giacca donna", "Women's Clothing", 110.0);

        Product p7 = new Product(7L, "T-shirt bambino", "Children's Clothing", 15.0);
        Product p8 = new Product(8L, "Pantaloni bambino", "Children's Clothing", 25.0);
        Product p9 = new Product(9L, "Felpa bambino", "Children's Clothing", 35.0);

        Product p10 = new Product(10L, "Cappello", "Accessories", 10.0);
        Product p11 = new Product(11L, "Cintura", "Accessories", 20.0);
        Product p12 = new Product(12L, "Zaino", "Accessories", 45.0);

        Product p13 = new Product(13L, "Scarpe da ginnastica", "Shoes", 60.0);
        Product p14 = new Product(14L, "Stivali donna", "Shoes", 90.0);
        Product p15 = new Product(15L, "Scarpe eleganti uomo", "Shoes", 100.0);

        List<Product> products = new ArrayList<>(List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15));

        // creo clienti
        Customer c1 = new Customer(111L, "Mario Rossi", 2);
        Customer c2 = new Customer(112L, "Giovanni Verdi", 3);
        Customer c3 = new Customer(113L, "Luigi Bianchi", 2);
        Customer c4 = new Customer(114L, "Anna Neri", 1);
        Customer c5 = new Customer(115L, "Paola Gialli", 3);
        Customer c6 = new Customer(116L, "Marco Viola", 1);
        Customer c7 = new Customer(117L, "Elena Blu", 2);
        Customer c8 = new Customer(118L, "Francesca Azzurri", 3);
        Customer c9 = new Customer(119L, "Riccardo Grigi", 1);
        Customer c10 = new Customer(120L, "Giulia Rosa", 2);

        // creo ordini
        Order o1 = new Order(11L, c1);
        Order o2 = new Order(12L, c2);
        Order o3 = new Order(13L, c3);
        Order o4 = new Order(14L, c1);
        Order o5 = new Order(15L, c3);
        Order o6 = new Order(16L, c9);
        Order o7 = new Order(15L, c2);
        Order o8 = new Order(17L, c9);
        Order o9 = new Order(18L, c9);

        // aggiungo prodotti agli ordini creati
        o1.getProducts().addAll(List.of(p1, p2, p13));
        o2.getProducts().addAll(List.of(p4, p6, p14));
        o3.getProducts().addAll(List.of(p3, p10, p12));
        o4.getProducts().addAll(List.of(p7, p8, p9));
        o5.getProducts().addAll(List.of(p11, p15, p5));
        o6.getProducts().addAll(List.of(p4, p6, p13));

        // creo lista di ordini
        List<Order> orders = new ArrayList<>(List.of(o1, o2, o3, o4, o5, o6, o7, o8, o9));


        System.out.println("--- ESERCIZIO 1 ---");
        Map<Customer, List<Order>> grupedByCustomer = orders.stream().collect(Collectors.groupingBy(Order::getCustomer));

        System.out.println("Lista ordini per cliente:");
        for (Map.Entry<Customer, List<Order>> entry : grupedByCustomer.entrySet()) {
            System.out.println(entry.getKey().getId() + " - " + entry.getKey().getName());
            for (Order order : entry.getValue()) {
                System.out.println(order);
            }
        }

        System.out.println("--- ESERCIZIO 2 ---");
        Map<Customer, Double> totalByCustomer = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingDouble(order -> order.getProducts().stream()
                        .mapToDouble(Product::getPrice).sum())));

        System.out.println("Lista totale per cliente:");
        for (Map.Entry<Customer, Double> entry : totalByCustomer.entrySet()) {
            System.out.println(entry.getKey().getId() + " - " + entry.getKey().getName() + " - Totale: " + entry.getValue());
        }

        System.out.println("--- ESERCIZIO 3 ---");
        List<Product> expensives = products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice).reversed())
                .limit(3).collect(Collectors.toList());

        System.out.println("Lista 3 prodotti più costosi");
        expensives.forEach(System.out::println);

        System.out.println("--- ESERCIZIO 4 ---");
        double average = orders.stream().mapToDouble(order -> order.getProducts().stream().mapToDouble(Product::getPrice).sum()).average().getAsDouble();

        System.out.println("Media totale ordini:");
        System.out.println(Math.floor(average));

        System.out.println("--- ESERCIZIO 5 ---");

        Map<String, Double> byCategory = products.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));

        System.out.println("Media per categoria:");
        for (Map.Entry<String, Double> entry : byCategory.entrySet()) {
            System.out.println("Categoria: " + entry.getKey() + " - Totale: " + entry.getValue());
        }

        System.out.println("--- ESERCIZIO 6 ---");

        try {
            saveProductsToLocalDisc(products);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("--- ESERCIZIO 7 ---");
        try {
            List<Product> newProductsList = readProductsFromLocalDisc();

            newProductsList.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void saveProductsToLocalDisc(List<Product> products) throws IOException {
        File file = new File(LOG_PRODOTTI_TXT);
        String prodotti = "";

        for (Product p : products) {
            prodotti += p.getId() + "@" + p.getName() + "@" + p.getCategory() + "@" + p.getPrice() + "#";
        }

        FileUtils.writeStringToFile(file, prodotti, "UTF-8");
    }

    public static List<Product> readProductsFromLocalDisc() throws IOException {
        File file = new File(LOG_PRODOTTI_TXT);

        String productsText = FileUtils.readFileToString(file, "UTF-8");
        String[] records = productsText.split("#");

        List<Product> products = new ArrayList<>();

        for(int i = 0; i < records.length; i++) {
            String[] fields = records[i].split("@");
            products.add(new Product(Long.parseLong(fields[0]), fields[1], fields[2], Double.parseDouble(fields[3])));
        }

        return products;
    }
}