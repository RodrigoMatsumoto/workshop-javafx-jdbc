package org.example.workshopjavafxjdbc.gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Utils {

  public static Stage currentStage(ActionEvent event) {
    return (Stage) ((Node) event.getSource()).getScene().getWindow();
  }

  public static Integer tryParseToInt(String str) {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
    tableColumn.setCellFactory(column ->
      new TableCell<>() {
        private final SimpleDateFormat sdf = new SimpleDateFormat(format);

        @Override
        protected void updateItem(Date item, boolean empty) {
          super.updateItem(item, empty);

          setText(empty ? null : sdf.format(item));
        }
      }
    );
  }

  public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
    tableColumn.setCellFactory(column ->
      new TableCell<>() {
        @Override
        protected void updateItem(Double item, boolean empty) {
          super.updateItem(item, empty);

          setText(empty ? null : String.format(Locale.US, ("%." + decimalPlaces + "f"), item));
          }
        }
    );
  }

  public static void formatDatePicker(DatePicker datePicker, String format) {
    datePicker.setConverter(new StringConverter<>() {
      final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
      {
        datePicker.setPromptText(format.toLowerCase());
      }

      @Override
      public String toString(LocalDate date) {
        return (date != null) ? dateFormatter.format(date) : null;
      }

      @Override
      public LocalDate fromString(String string) {
        return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
      }
    });
  }

  public static Double tryParseToDouble(String str) {
    try {
      return Double.parseDouble(str);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}