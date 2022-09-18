import {PipeTransform, Pipe} from "@angular/core";
import {DatePipe} from "@angular/common";

@Pipe({name: 'twitterTimestamp'})
export class TwitterTimestampPipe implements PipeTransform {
  readonly second: number = 1000;
  readonly minute: number = 1000 * 60;
  readonly hour: number = this.minute * 60;
  readonly day: number = this.hour * 24;

  constructor() { }

  transform(input: number, args: string[]): any {
    let difference: number = this.currentTime().getTime() - this.getUtcTimeFor(input).getTime();
    if (difference < this.day) {
      if (difference < this.minute) {
        let value = (difference / this.second).toFixed(0);
        return "" + value + "s";
      }

      if (difference < this.hour) {
        let value = (difference / this.minute).toFixed(0);
        return "" + value + "m";

      }

      let value = (difference / this.hour).toFixed(0);
      return "" + value + "h";
    }

    return new DatePipe("en-US").transform(this.getUtcTimeFor(input), 'MMM d, HH:mm');
  }

  // TODO: revisit this mess...
  private currentTime(): Date {
    let result = this.getUtcTimeForDate(new Date());
    result.setHours(result.getHours() + 2)
    return result;
  }

  private getUtcTimeForDate(input: Date): Date {
    input.setMinutes(input.getMinutes() + input.getTimezoneOffset());
    return input;
  }

  private getUtcTimeFor(input: number): Date {
    return this.getUtcTimeForDate(new Date(input));
  }

}
