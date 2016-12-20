import {Injectable} from "@angular/core";
import {Http, Headers} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Definition} from "../model/definition";

@Injectable()
export class DefinitionService {

  private baseUrl = 'http://localhost:8080/api/definitions';
  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(private http: Http) {
  }

  getDefinitions(): Promise<Definition[]> {
    return this.http.get(this.baseUrl)
      .toPromise()
      .then(response => response.json().content as Definition[])
      .catch(this.handleError);
  }

  getDefinition(id: number): Promise<Definition> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get(url)
      .toPromise()
      .then(response => response.json() as Definition)
      .catch(this.handleError);
  }


  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error);

    return Promise.reject(error.message || error);
  }
}
