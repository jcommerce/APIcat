import {Injectable} from "@angular/core";
import {Observable, BehaviorSubject} from "rxjs";
import {Http} from "@angular/http";

@Injectable()
export class ApiFormatService {
  formats: Observable<string[]>;
  private _formats: BehaviorSubject<string[]>;
  private dataStore: {
    formats: string[]
  };

  private baseUrl = '/api/formats';

  constructor(private http: Http) {
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
        error => console.error('Could not load api formats.')
      );
  }

}
