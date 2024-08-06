package com.b07project2024.group1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * The CatalogItem class is POJO that represents an item in the TAAM collection
 */
public class CatalogItem {
    private String lot;
    private String name;
    private String category;
    private String period;
    private String description;
    private String pictureURL;

    public CatalogItem() {}

    public CatalogItem(String lot, String name, String category, String description, String period, String pictureURL) {
        this.lot = lot;
        this.name = name;
        this.category = category;
        this.description = description;
        this.period = period;
        this.pictureURL = pictureURL;
    }


    // Getters and setters
    public String getLot() { return lot; }
    public void setLot(String lot) { this.lot = lot; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPictureURL() { return pictureURL; }
    public void setPictureURL(String pictureURL) { this.pictureURL = pictureURL; }

    /**
     *  Checks if two CatalogItem instances are equal in lot number and content
     * @param obj any Object
     * @return true if equal and false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (!(obj instanceof CatalogItem)) {
            return false;
        }
        CatalogItem other = (CatalogItem) obj;
        return lot.equals(other.lot)
                && name.equals(other.name)
                && category.equals(other.category)
                && description.equals(other.description)
                && period.equals(other.period)
                && pictureURL.equals(other.pictureURL);
    }

    /**
     * String representation of CatalogItem
     * @return 'lot_name_category_description'
     */
    @NonNull
    @Override
    public String toString() {
        return Objects.toString(lot, "")  +
                Objects.toString(name, "") + '_' +
                Objects.toString(category, "") + '_' +
                Objects.toString(description, "");
    }

    /**
     * Hash code of the individual hashes in an array
     * @return hash code of the individual hashes in an array
     */
    @Override
    public int hashCode(){
        return Objects.hash(lot, name, category, description, period, pictureURL);
    }

    /**
     * Checks if this item is equals to the search param "item"
     * @param item is the search parameters
     * @return true if matching false if not
     */
    public boolean itemMatchesSearchParams(CatalogItem item){
        if (this.getLot() != null)
            if (!this.getLot().equals(item.getLot()))
                return false;
        if (this.getName() != null)
            if (!this.getName().equals(item.getName()))
                return false;
        if (this.getCategory() != null)
            if (!this.getCategory().equals(item.getCategory()))
                return false;
        if (this.getPeriod() != null)
            return this.getPeriod().equals(item.getPeriod());
        return true;
    }
    
}
