/**
 * Created by zhangle on 2017/3/15.
 */
export class StringUtil {

  public static isEmpty(arg: string): boolean {
    return arg === undefined || arg === null || arg.trim() === '';
  }

  public static isNotEmpty(arg: string): boolean {
    return !StringUtil.isEmpty(arg);
  }

}
