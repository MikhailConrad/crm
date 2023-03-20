package ru.sevavto.stock.crm.util;

import ru.sevavto.stock.crm.model.dto.analytic.Rating;

import java.util.Comparator;

public class ComparatorByRating implements Comparator<Rating> {
    @Override
    public int compare(Rating r1, Rating r2) {
        if(r1.getRating() < r2.getRating())
            return -1;
        else if(r1.getRating() > r2.getRating())
            return 1;
        return 0;
    }
}
