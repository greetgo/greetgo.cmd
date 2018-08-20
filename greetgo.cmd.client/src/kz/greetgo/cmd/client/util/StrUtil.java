package kz.greetgo.cmd.client.util;

public class StrUtil {
  public static String firstLower(String s) {
    if (s == null) return null;
    if (s.length() == 0) return "";
    return s.substring(0, 1).toLowerCase() + s.substring(1);
  }

  public static String toUnderscore(String s) {
    if (s == null) return null;

    int length = s.length();

    if (length == 0) return "";

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
}
