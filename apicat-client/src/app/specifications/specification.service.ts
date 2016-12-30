import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/toPromise';
import {Specification} from '../model/specification';
import {CrudService} from '../shared/crud.service';

@Injectable()
export class SpecificationService extends CrudService<Specification> {

  constructor(http: Http) {
    super(http);
  }

  protected getBaseUrl(): string {
    return '/api/specifications';
  }
}
