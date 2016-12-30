import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ValidationResult} from '../model/validation-result';

@Injectable()
export class ValidationService {

  constructor() {
  }

  validateDefinitionContent(content: string, type: string): Observable<ValidationResult> {
    let status = this.getRandomBoolean();
    let remarks = status ? [] : this.getRandomRemarks();

    return Observable.of(new ValidationResult(status, remarks)).delay(1000);
  }

  private getRandomRemarks() {
    let remarks = [];
    let options = [
      'Version is missing',
      'The host (name or ip) of the API is missing',
      'Invalid metadata',
      'Bad indentation of a mapping entry at line 12, column 8'
    ];

    for (let option of options) {
      if (this.getRandomBoolean()) {
        remarks.push(option);
      }
    }

    if (remarks.length === 0) {
      remarks.push(options[0]);
    }

    return remarks;
  }

  private getRandomBoolean(): boolean {
    return Math.random() >= 0.5;
  }
}
