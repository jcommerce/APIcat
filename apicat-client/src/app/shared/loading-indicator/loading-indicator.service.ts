import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable()
export class LoadingIndicatorService {
  visible: Observable<boolean>;
  private _visible: BehaviorSubject<boolean>;
  private dataStore: {
    visible: boolean
  };

  constructor() {
    let defaultValue = false;

    this.dataStore = {visible: defaultValue};
    this._visible = <BehaviorSubject<boolean>>new BehaviorSubject(defaultValue);
    this.visible = this._visible.asObservable();
  }

  public showSpinner() {
    this.updateVisible(true);
  }

  public hideSpinner() {
    this.updateVisible(false);
  }

  private updateVisible(visible: boolean) {
    this.dataStore.visible = visible;
    this._visible.next(Object.assign({}, this.dataStore).visible);
  }
}
