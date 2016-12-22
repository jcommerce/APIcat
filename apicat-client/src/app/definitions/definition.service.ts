import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Definition} from "../model/definition";
import {Observable} from "rxjs";

@Injectable()
export class DefinitionService {

  private baseUrl = '/api/definitions';
  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(private http: Http) {
  }

  getDefinitions(): Observable<Definition[]> {
    return this.http.get(this.baseUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  getDefinition(id: number): Observable<Definition> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get(url)
      .map(this.extractData)
      .catch(this.handleError);
  }

  create(definition: Definition): Observable<Definition> {
    return this.http
      .post(this.baseUrl, JSON.stringify(definition), {headers: this.headers})
      .map(this.extractData)
      .catch(this.handleError);
  }

  update(definition: Definition): Observable<Definition> {
    const url = `${this.baseUrl}/${definition.id}`;
    return this.http
      .put(url, JSON.stringify(definition), {headers: this.headers})
      .map(() => definition)
      .catch(this.handleError);
  }

  delete(id: number): Observable<void> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url, {headers: this.headers})
      .map(() => null)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    let body = res.json();
    return body.data || {};
  }

  private handleError(error: Response | any) {
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);

    return Observable.throw(errMsg);
  }

}
