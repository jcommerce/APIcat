import {Component, OnInit} from '@angular/core';
import {ValidationService} from './validation.service';
import {LoadingIndicatorService} from '../shared/loading-indicator/loading-indicator.service';
import {Observable} from 'rxjs';
import {ApiFormatService} from '../shared/api-format.service';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {ValidationResult} from '../model/validation-result';

@Component({
  selector: 'app-validation-component',
  templateUrl: './validation.component.html',
  styleUrls: ['./validation.component.scss'],

})
export class ValidationComponent implements OnInit {

  content: string;
  selectedFormat: string;
  form: FormGroup;
  validationResult: ValidationResult;
  formats: Observable<string[]>;

  constructor(private validationService: ValidationService,
              private apiFormatService: ApiFormatService,
              private loadingService: LoadingIndicatorService,
              fb: FormBuilder) {
    this.form = fb.group({
      'format': [null, Validators.required],
      'content': [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.formats = this.apiFormatService.formats;
  }

  validate(): void {
    this.loadingService.showSpinner();
    this.validationService.validateDefinitionContent(this.content, this.selectedFormat)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(result => this.validationResult = result);
  }


}
