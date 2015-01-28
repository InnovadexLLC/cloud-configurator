package com.sciul.cloud_configurator;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

/**
 * Created by sumeetrohatgi on 1/28/15.
 */
@JadeHelper
public class Util {
  @Autowired
  private MessageSource messageSource;

  public String message(String code) {
    return messageSource.getMessage(code, null, "not found: '${code}'", getLocale());
  }

  public String formatNumber(BigDecimal number, String format) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    DecimalFormat decimalFormat = new DecimalFormat(format, symbols);
    decimalFormat.setParseBigDecimal(true);

    return decimalFormat.format(number);
  }

}
