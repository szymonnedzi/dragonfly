package simon.dragonfly.model;

public class DeliveryOrder {
    private String id;
    private String type;
    private Integer[] size;
    private Delivery delivery;

    public DeliveryOrder(String id, String type, Integer[] size, Delivery coordinates) {
        this.id = id;
        this.type = type;
        this.size = size;
        this.delivery = coordinates;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Integer[] getSize() {
        return size;
    }

    public Delivery getDelivery() {
        return delivery;
    }

}
