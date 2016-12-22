import {Injectable} from "@angular/core";
import {Http, Headers} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Definition} from "../model/definition";

@Injectable()
export class DefinitionService {

  private baseUrl = '/api/definitions';
  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(private http: Http) {
  }

  getDefinitions(): Promise<Definition[]> {
    return this.http.get(this.baseUrl)
      .toPromise()
      .then(response => response.json().data as Definition[])
      .catch(this.handleError);
  }

  getDefinition(id: number): Promise<Definition> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get(url)
      .toPromise()
      .then(response => response.json().data as Definition)
      .catch(this.handleError);
  }

  create(definition: Definition): Promise<Definition> {
    return this.http
      .post(this.baseUrl, JSON.stringify(definition), {headers: this.headers})
      .toPromise()
      .then(res => res.json().data)
      .catch(this.handleError);
  }

  update(definition: Definition): Promise<Definition> {
    const url = `${this.baseUrl}/${definition.id}`;
    return this.http
      .put(url, JSON.stringify(definition), {headers: this.headers})
      .toPromise()
      .then(() => definition)
      .catch(this.handleError);
  }

  delete(id: number): Promise<void> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers})
      .toPromise()
      .then(() => null)
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error);

    return Promise.reject(error.message || error);
  }
}
