import {Injectable} from "@angular/core";
import {Observable, BehaviorSubject} from "rxjs";
import {Http} from "@angular/http";

@Injectable()
export class ApiStagesService {
  stages: Observable<string[]>;
  private _stages: BehaviorSubject<string[]>;
  private dataStore: {
    stages: string[]
  };

  private baseUrl = '/api/specificationStages';

  constructor(private http: Http) {
    this.dataStore = {stages: []};
    this._stages = <BehaviorSubject<string[]>>new BehaviorSubject([]);
    this.stages = this._stages.asObservable();

    this.loadAll();
  }

  loadAll(): void {
    this.http.get(this.baseUrl)
      .map(response => response.json().data)
      .subscribe(
        data => {
          this.dataStore.stages = data;
          this._stages.next(Object.assign({}, this.dataStore).stages);
        },
        error => console.error('Could not load api stages.')
      );
  }

}
