package utils;

import javafx.scene.control.ListCell;
import javafx.scene.control.TableRow;
import javafx.scene.paint.Color;
import model.Order;

import java.text.NumberFormat;

/**
 * Created by rostunov on 11.08.15.
 */
public class ColorFormatCell extends TableRow<Order> {

    public ColorFormatCell() {    }

    @Override protected void updateItem(Order item, boolean empty) {
        // calling super here is very important - don't skip this!
        super.updateItem(item, empty);

        // format the number as if it were a monetary value using the
        // formatting relevant to the current locale. This would format
        // 43.68 as "$43.68", and -23.67 as "-$23.67"
        //setText(item == null ? "" : NumberFormat.getCurrencyInstance().format(item));

        // change the text fill based on whether it is positive (green)
        // or negative (red). If the cell is selected, the text will
        // always be white (so that it can be read against the blue
        // background), and if the value is zero, we'll make it black.
        if (item != null) {
            String value = item.getStatus_color();
            setTextFill(Color.web(value));
            /*
            setTextFill(isSelected() ? Color.WHITE :
                    value == 0 ? Color.BLACK :
                            value < 0 ? Color.RED : Color.GREEN);
            */
        }
    }
}

