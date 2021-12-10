package jav.app.monthlygrandtest1.Model;

public class Products {

        public int id;
        public String title;
        public double price;
        public String description;
        public String category;
        public String image;
        public Rating rating;
        public double rate;
        public int count;

    public Products(int id, String title, double price, String description, String category, String image, double rate, int count) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
        this.rate = rate;
        this.count = count;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public Rating getRating() {
        return rating;
    }

    public double getRate(){
        return rate;
    }
    public int getCount(){
        return count;
    }
}

 class Rating{
    public double rate;
    public int count;
}
