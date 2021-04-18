package com.raven.trcrypto.Model;

public class CoinModel {

    public String name;
    public String symbol;
    public String current_price;
    public String image;
    public String price_change_percentage_1h_in_currency;
    public String price_change_percentage_24h_in_currency;
    public String price_change_percentage_7d_in_currency;

    public CoinModel( String name, String symbol, String current_price, String image, String price_change_percentage_1h_in_currency, String price_change_percentage_24h_in_currency, String price_change_percentage_7d_in_currency) {

        this.name = name;
        this.symbol = symbol;
        this.current_price = current_price;
        this.image = image;
        this.price_change_percentage_1h_in_currency = price_change_percentage_1h_in_currency;
        this.price_change_percentage_24h_in_currency = price_change_percentage_24h_in_currency;
        this.price_change_percentage_7d_in_currency = price_change_percentage_7d_in_currency;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String price_usd) {
        this.current_price = current_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice_change_percentage_1h_in_currency() {
        return price_change_percentage_1h_in_currency;
    }

    public void setPrice_change_percentage_1h_in_currency(String price_change_percentage_1h_in_currency) {
        this.price_change_percentage_1h_in_currency = price_change_percentage_1h_in_currency;
    }

    public String getPrice_change_percentage_24h_in_currency() {
        return price_change_percentage_24h_in_currency;
    }

    public void setPrice_change_percentage_24h_in_currency(String price_change_percentage_24h_in_currency) {
        this.price_change_percentage_24h_in_currency = price_change_percentage_24h_in_currency;
    }

    public String getPrice_change_percentage_7d_in_currency() {
        return price_change_percentage_7d_in_currency;
    }

    public void setPrice_change_percentage_7d_in_currency(String price_change_percentage_7d_in_currency) {
        this.price_change_percentage_7d_in_currency = price_change_percentage_7d_in_currency;
    }
}
