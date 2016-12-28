import {Component, OnInit, Input, Output, EventEmitter} from "@angular/core";
import {ApiFormatService} from "../../common/api-format.service";
import {Observable} from "rxjs";
import {Specification} from "../../model/specification";
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'specification-form',
  templateUrl: './specification-form.component.html',
  styleUrls: ['./specification-form.component.scss'],

})
export class SpecificationFormComponent implements OnInit {

  @Input() specification: Specification;
  @Output('submitSpecification') submitEmitter = new EventEmitter<Specification>();

  formats: Observable<string[]>;
  specificationForm: FormGroup;

  constructor(private apiFormatService: ApiFormatService,
              fb: FormBuilder) {
    this.specificationForm = fb.group({
      'name': [null, Validators.required],
      'author': [null, Validators.required],
      'version': [null, Validators.required],
      'format': [null, Validators.required],
      'content': [null, Validators.required]
    })
  }

  ngOnInit(): void {
    this.formats = this.apiFormatService.formats;
  }

  onSubmit(): void {
    this.submitEmitter.emit(this.specification);
  }
}
