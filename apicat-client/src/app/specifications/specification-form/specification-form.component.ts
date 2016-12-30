import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {ApiFormatService} from '../../shared/api-format.service';
import {Observable} from 'rxjs';
import {Specification} from '../../model/specification';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {ApiStagesService} from '../../shared/api-stages.service';

@Component({
  selector: 'app-specification-form',
  templateUrl: './specification-form.component.html',
  styleUrls: ['./specification-form.component.scss'],

})
export class SpecificationFormComponent implements OnInit {

  @Input() specification: Specification;
  @Output() submitSpecification = new EventEmitter<Specification>();

  formats: Observable<string[]>;
  stages: Observable<string[]>;
  specificationForm: FormGroup;

  constructor(private apiFormatService: ApiFormatService,
              private apiStagesService: ApiStagesService,
              fb: FormBuilder) {
    this.specificationForm = fb.group({
      'name': [null, Validators.required],
      'author': [null, Validators.required],
      'version': [null, Validators.required],
      'format': [null, Validators.required],
      'stage': [null, Validators.required],
      'content': [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.formats = this.apiFormatService.formats;
    this.stages = this.apiStagesService.stages;
  }

  onSubmit(): void {
    this.submitSpecification.emit(this.specification);
  }
}
