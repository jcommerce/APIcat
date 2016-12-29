import {Injectable} from "@angular/core";
import {Observable, BehaviorSubject} from "rxjs";
import {Http} from "@angular/http";
import {AlertMessageService} from "./alert/alert-message.service";

@Injectable()
export class ApiStagesService {
  stages: Observable<string[]>;
  private _stages: BehaviorSubject<string[]>;
  private dataStore: {
    stages: string[]
  };

  private readonly baseUrl = '/api/specificationStages';

  constructor(private http: Http, private alertMessageService: AlertMessageService) {
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
        error => this.handleError()
      );
  }

  private handleError(): void {
    this.alertMessageService.showErrorMessage("Unable to load api specification stages");
  }

}
