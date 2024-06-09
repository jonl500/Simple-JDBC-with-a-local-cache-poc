package com.ProductDb;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        DbHandler dbHandler = new DbHandler();
        dbHandler.connect();
        dbHandler.retrieveData();
        Scanner sc = new Scanner(System.in);
        System.out.println("Press q to exit");
        System.out.println("to add product press 1");
        System.out.println("to view product press 2");
        System.out.println("to update product press 3");
        System.out.println("to delete product press 4");
        dbHandler.retrieveData();
        dbHandler.getProducts().forEach((k, v) -> System.out.println(k + " " + v.toString()));
        String input1 = sc.nextLine();
        while (!"q".equals(input1)) {
            switch (input1) {
                case "1":
                    System.out.println("enter your product id");
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.println("enter your product name");
                    String name = sc.nextLine();
                    System.out.println("enter your product price");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.println("enter your product stock");
                    int stock = Integer.parseInt(sc.nextLine());
                    Product product = new Product(name, price, stock, id);
                    dbHandler.addProduct(product);
                    break;
                case "2":
                    System.out.println("please enter your product id");
                    int id1 = Integer.parseInt(sc.nextLine());
                    Product product1 = dbHandler.viewProduct(id1);
                    System.out.println(product1.toString());
                    break;

                case "3":

                    System.out.println("please enter your product id to update");
                    input1 = sc.nextLine();
                    int id2 = Integer.parseInt(input1);
                    Product p = dbHandler.getProducts().computeIfPresent(id2, (k, v) -> {
                        System.out.println("Enter new product name");
                        String newName = sc.nextLine();
                        System.out.println("enter new product price");
                        double newPrice = Double.parseDouble(sc.nextLine());
                        System.out.println("enter new product stock");
                        int newStock = Integer.parseInt(sc.nextLine());
                        Product newProduct = new Product(newName, newPrice, newStock, id2);
                        return newProduct;
                    });

                    break;

                case "4":
                    System.out.println("please enter your product id to delete");
                    input1 = sc.nextLine();
                    int id3 = Integer.parseInt(input1);
                    dbHandler.getProducts().remove(id3);
                    break;


                default:
                    System.out.println("invalid input");

                    }


            System.out.println("Press q to exit");
            System.out.println("to add product press 1");
            System.out.println("to view product press 2");
            System.out.println("to update product press 3");
            System.out.println("to delete product press 4");
            input1 = sc.nextLine();
            }
        dbHandler.moveData();
        dbHandler.removeDeletedRows();
        dbHandler.close();
        }
    }
