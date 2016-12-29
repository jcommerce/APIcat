type AlertType = 'success' | 'info' | 'warning' | 'danger';

export class AlertMessage {
  content: string;
  type: AlertType;
  timeToLive: number = 0;

  constructor(content: string, type: AlertType, timeToLive?: number) {
    this.content = content;
    this.type = type;
    if (typeof timeToLive === "number") {
      this.timeToLive = timeToLive;
    }
  }

}
