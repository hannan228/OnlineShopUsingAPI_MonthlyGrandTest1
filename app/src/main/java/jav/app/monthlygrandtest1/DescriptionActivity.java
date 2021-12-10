package jav.app.monthlygrandtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DescriptionActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName,productPrice,productDescription,rateDescription,countDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        variableInitialization();
    }

    public void variableInitialization(){
        productImage = findViewById(R.id.productImageDesc);
        productName = findViewById(R.id.productNameDesc);
        productPrice = findViewById(R.id.productPriceDesc);
        productDescription = findViewById(R.id.productDescriptionTextDesc);
        rateDescription = findViewById(R.id.rateDescription);
        countDescription = findViewById(R.id.countDescription);

        String url = getIntent().getStringExtra("productImage");
        productName.setText(getIntent().getStringExtra("productName"));
        productPrice.setText("Rs: "+getIntent().getStringExtra("productPrice"));
        productDescription.setText(""+getIntent().getStringExtra("productDescription"));
        rateDescription.setText(""+getIntent().getStringExtra("ratting"));
        countDescription.setText("("+getIntent().getStringExtra("count")+")");
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_baseline_image_24)
                .fitCenter()
                .error(R.drawable.ic_baseline_image_24)
                .into(productImage);
    }

}