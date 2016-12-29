import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Definition} from "../model/definition";
import {CrudService} from "../shared/crud.service";

@Injectable()
export class DefinitionService extends CrudService<Definition> {

  constructor(http: Http) {
    super(http);
  }

  protected getBaseUrl(): string {
    return '/api/definitions';
  }
}
