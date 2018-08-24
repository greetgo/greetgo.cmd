package kz.greetgo.cmd.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StrUtil {
  public static String firstLower(String s) {
    if (s == null) { return null; }
    if (s.length() == 0) { return ""; }
    return s.substring(0, 1).toLowerCase() + s.substring(1);
  }

  public static String toUnderscore(String s) {
    if (s == null) { return null; }

    int length = s.length();

    if (length == 0) { return ""; }

    char arr[] = s.toCharArray();

    int start = -1, end = -1;

    for (int i = 0; i < length; i++) {
      char c = arr[i];
      char lowC = Character.toLowerCase(arr[i]);
      if (c != lowC) {
        if (start < 0) {
          start = end = i;
        } else {
          end = i;
        }
      } else if (start > -1) {
        for (int j = start + 1; j < end; j++) {
          arr[j] = Character.toLowerCase(arr[j]);
        }
        start = end = -1;
      }
    }

    if (start > -1) {
      for (int j = start + 1; j <= end; j++) {
        arr[j] = Character.toLowerCase(arr[j]);
      }
    }

    char result[] = new char[length * 2];
    int index = 1;

    result[0] = Character.toLowerCase(arr[0]);

    for (int i = 1; i < length; i++) {
      char c = arr[i];
      char lowC = Character.toLowerCase(arr[i]);
      if (c != lowC) {
        result[index++] = '_';
      }
      result[index++] = lowC;
    }

    return new String(result, 0, index);
  }

  public static String spaces(int len) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append(' ');
    }
    return sb.toString();
  }

  public static boolean toBool(String value) {
    if (value == null) {
      return false;
    }

    value = value.trim().toLowerCase();

    return "true".equals(value)
      || "t".equals(value)
      || "да".equals(value)
      || "д".equals(value)
      || "истина".equals(value)
      || "и".equals(value)
      || "真相".equals(value)
      || "是的".equals(value)
      || "on".equals(value)
      || "yes".equals(value)
      || "y".equals(value)
      || "1".equals(value);
  }

  public static List<String> splitOnLines(String str) {

    List<String> ret = new ArrayList<>(Arrays.asList(str.split("\n")));

    if (str.endsWith("\n")) {
      ret.add("");
    }

    return ret;
  }
}
