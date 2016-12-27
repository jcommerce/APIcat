import {Http, Response, Headers} from "@angular/http";
import {Observable} from "rxjs";
import {CrudEntity} from "../model/crud-entity";

export abstract class CrudService<T extends CrudEntity> {

  protected headers = new Headers({'Content-Type': 'application/json'});

  constructor(protected http: Http) {
  }

  protected abstract getBaseUrl(): string;

  public delete(id: number): Observable<void> {
    const url = `${this.getBaseUrl()}/${id}`;
    return this.http.delete(url, {headers: this.headers})
      .map(() => null)
      .catch(this.handleError);
  }

  public getAll(): Observable<T[]> {
    return this.http.get(this.getBaseUrl())
      .map(this.extractData)
      .catch(this.handleError);
  }

  public getOne(id: number): Observable<T> {
    const url = `${this.getBaseUrl()}/${id}`;
    return this.http.get(url)
      .map(this.extractData)
      .catch(this.handleError);
  }

  public create(thing: T): Observable<T> {
    return this.http
      .post(this.getBaseUrl(), JSON.stringify(thing), {headers: this.headers})
      .map(this.extractData)
      .catch(this.handleError);
  }

  public update(thing: T): Observable<T> {
    const url = `${this.getBaseUrl()}/${thing.id}`;
    return this.http
      .put(url, JSON.stringify(thing), {headers: this.headers})
      .map(() => thing)
      .catch(this.handleError);
  }

  protected extractData(res: Response) {
    let body = res.json();
    return body.data || {};
  }

  protected handleError(error: Response | any) {
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
