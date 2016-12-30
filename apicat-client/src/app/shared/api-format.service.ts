import {Injectable} from '@angular/core';
import {Observable, BehaviorSubject} from 'rxjs';
import {Http} from '@angular/http';
import {AlertMessageService} from './alert/alert-message.service';

@Injectable()
export class ApiFormatService {
  formats: Observable<string[]>;
  private _formats: BehaviorSubject<string[]>;
  private dataStore: {
    formats: string[]
  };

  private readonly baseUrl = '/api/formats';

  constructor(private http: Http, private alertMessageService: AlertMessageService) {
    this.dataStore = {formats: []};
    this._formats = <BehaviorSubject<string[]>>new BehaviorSubject([]);
    this.formats = this._formats.asObservable();

    this.loadAll();
  }

  loadAll(): void {
    this.http.get(this.baseUrl)
      .map(response => response.json().data)
      .subscribe(
        data => {
          this.dataStore.formats = data;
          this._formats.next(Object.assign({}, this.dataStore).formats);
        },
        error => this.handleError()
      );
  }

  private handleError(): void {
    this.alertMessageService.showErrorMessage('Unable to load api formats');
  }
}
