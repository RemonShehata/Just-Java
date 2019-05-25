package com.example.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void SubmitOrder(View view) {

        //get the views by their IDs
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        EditText nameEditText = findViewById(R.id.name_edit_text);

        //check if the customer checked any of the checkboxes
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        boolean hasChocolate = chocolateCheckBox.isChecked();
        //get the customer name
        String name = nameEditText.getText().toString();

        //get the price of the coffee ordered
        int price = calculatePrice(hasWhippedCream, hasChocolate);

        //get the order summary
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

        //send the order via email using intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.order_summary_name) + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {

        //the max quantity of coffees is 100
        if (quantity == 100) {
            Toast.makeText(getApplicationContext(), "You cannot have more than 100 coffees",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {

        // the min quantity of the coffee is 1 cup of coffee
        if (quantity == 1) {
            Toast.makeText(getApplicationContext(), "Cannot be less than 1 Coffee",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(number);
    }

    /**
     * calculates the price of the order
     *
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {

        //price per cup
        int basePrice = 5;
        if (addWhippedCream) {
            basePrice += 1;
        }
        if (addChocolate) {
            basePrice += 2;
        }

        //total price = price per cup x quantity
        return basePrice * quantity;
    }

    /**
     * Create summary of the order
     *
     * @param price           of the order
     * @param addWhippedCream is whether or not the user wants a whipped cream topping
     * @param addChocolate    is whether or not the user wants a chocolate topping
     * @param name            of the customer
     * @return text summary
     */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String name) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.quantity) + ": " + quantity;
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream) + addWhippedCream;
        priceMessage += "\n" + getString(R.string.order_summary_chocolate) + addChocolate;
        priceMessage += "\n" + getString(R.string.order_summary_price) + price;
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

}
