package com.sciul.cloud_configurator.dsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains common conventions for defining CIDR blocks
 *
 * Created by sumeetrohatgi on 1/2/15.
 */
public class CidrUtils {
  private final static Logger logger = LoggerFactory.getLogger(CidrUtils.class);

  private String cidrBlock;

  private CidrUtils(String cidrBlock) {
    this.cidrBlock = cidrBlock;
  }

  /**
   * initialize a new CIDR block
   * @param cidrBlock
   * @return
   */
  public static CidrUtils build(String cidrBlock) {
    return new CidrUtils(cidrBlock);
  }

  /**
   * generate a new CIDR block based on passed in maskBits
   *
   * @param maskBits
   * @return
   */
  public static CidrUtils build(int maskBits) {
    return new CidrUtils(maskBits);
  }

  private CidrUtils(int maskBits) {
    if (maskBits < 16 || maskBits % 8 != 0 )
      throw new IllegalArgumentException("please ensure maskBits to be a multiple of 8 and no less than 16");
    cidrBlock = "10.0.0.0/" + maskBits;
  }

  private int[] components() {
    String[] components = cidrBlock.split("\\.|/");
    if (components.length != 5) {
      logger.debug("components: {}, length: {}", (String[])components, components.length);
      throw new IllegalArgumentException("incorrect cidrBlock passed in, unable to parse!");
    }

    int[] octets = new int[5];
    int i = 0;
    for (String component : components) {
      int octet = Integer.parseInt(component);

      if (octet > 255 || octet < 0) {
        throw new IllegalArgumentException("bad cidr block passed in!");
      }

      if (i == 4 && (octet < 0 || octet > 32)) {
        throw new IllegalArgumentException("maskBits is incorrect in cidr block!");
      }
      octets[i++] = octet;
    }

    return octets;
  }

  /**
   * increment subnet based on new maskBits passed in
   *
   * @param maskBits
   * @return
   */
  public String incrementSubnet(int maskBits) {
    int[] octets = components();

    int octalNumber = maskBits / 8 - 1;
    octets[octalNumber] = octets[octalNumber] + 1;
    cidrBlock = String.format("%d.%d.%d.%d/%d", octets[0], octets[1], octets[2], octets[3], maskBits);
    return cidrBlock;
  }

  /**
   * change maskBits for the stored cidrBlock
   *
   * @param maskBits
   * @return
   */
  public CidrUtils changeMask(int maskBits) {
    if (maskBits < 0 || maskBits > 32) {
      throw new IllegalArgumentException("illegal maskBits specified for ipv4");
    }

    int octets[] = components();
    cidrBlock = String.format("%d.%d.%d.%d/%d", octets[0], octets[1], octets[2], octets[3], maskBits);
    return this;
  }

  /**
   * increment subnet number based on the original maskBits setup
   *
   * @return
   */
  public String incrementSubnet() {
    return incrementSubnet(components()[4]);
  }

  public String toString() {
    return cidrBlock;
  }

}
