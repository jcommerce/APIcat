import {Injectable} from '@angular/core';
import {AlertMessage} from './alert-message';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable()
export class AlertMessageService {
  alerts: Observable<AlertMessage[]>;
  private _alerts: BehaviorSubject<AlertMessage[]>;
  private dataStore: {
    alerts: AlertMessage[]
  };

  constructor() {
    this.dataStore = {alerts: []};
    this._alerts = <BehaviorSubject<AlertMessage[]>>new BehaviorSubject([]);
    this.alerts = this._alerts.asObservable();
  }

  public addAlert(alert: AlertMessage) {
    this.dataStore.alerts.push(alert);
    this._alerts.next(Object.assign({}, this.dataStore).alerts);
  }

  public closeAlert(alert: AlertMessage) {
    let index = this.dataStore.alerts.indexOf(alert);
    this.dataStore.alerts.splice(index, 1);

    this._alerts.next(Object.assign({}, this.dataStore).alerts);
  }

  public closeAllAlerts() {
    if (this.dataStore.alerts.length > 0) {
      this.dataStore.alerts = [];
      this._alerts.next(Object.assign({}, this.dataStore).alerts);
    }
  }

  public showErrorMessage(message: string): void {
    let alert = new AlertMessage(message, 'danger');
    this.addAlert(alert);
  }
}
