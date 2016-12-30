import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {Definition} from '../../model/definition';
import {ApiFormatService} from '../../shared/api-format.service';
import {Observable} from 'rxjs';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';

@Component({
  selector: 'app-definition-form',
  templateUrl: './definition-form.component.html',
  styleUrls: ['./definition-form.component.scss'],

})
export class DefinitionFormComponent implements OnInit {

  @Input() definition: Definition;
  @Output() submitDefinition = new EventEmitter<Definition>();

  formats: Observable<string[]>;
  definitionForm: FormGroup;

  constructor(private apiFormatService: ApiFormatService,
              fb: FormBuilder) {
    this.definitionForm = fb.group({
      'name': [null, Validators.required],
      'author': [null, Validators.required],
      'version': [null, Validators.required],
      'format': [null, Validators.required],
      'content': [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.formats = this.apiFormatService.formats;
  }

  onSubmit(): void {
    this.submitDefinition.emit(this.definition);
  }
}
