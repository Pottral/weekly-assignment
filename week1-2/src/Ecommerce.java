public class Ecommerce {

    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        manager.addProduct("IPHONE15_256GB", 100);

        manager.checkStock("IPHONE15_256GB");

        manager.purchaseItem("IPHONE15_256GB", 12345);
        manager.purchaseItem("IPHONE15_256GB", 67890);

        for(int i = 0; i < 100; i++){
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        manager.purchaseItem("IPHONE15_256GB", 99999);

    }
}