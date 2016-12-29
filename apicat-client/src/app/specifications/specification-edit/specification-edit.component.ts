import {Component, OnInit} from "@angular/core";
import {Router, Params, ActivatedRoute} from "@angular/router";
import {AlertMessageService} from "../../shared/alert/alert-message.service";
import {LoadingIndicatorService} from "../../shared/loading-indicator/loading-indicator.service";
import {Specification} from "../../model/specification";
import {SpecificationService} from "../specification.service";

@Component({
  selector: 'specification-edit',
  templateUrl: './specification-edit.component.html',
  styleUrls: ['./specification-edit.component.scss'],

})
export class SpecificationEditComponent implements OnInit {

  specification: Specification;

  constructor(private specificationService: SpecificationService,
              private router: Router,
              private route: ActivatedRoute,
              private alertMessageService: AlertMessageService,
              private loadingService: LoadingIndicatorService) {
  }

  ngOnInit(): void {
    this.loadingService.showSpinner();

    this.route.params
      .switchMap((params: Params) =>
        this.specificationService.getOne(+params['id']).finally(() => this.loadingService.hideSpinner())
      )
      .subscribe(
        specification => this.specification = specification,
        error => this.alertMessageService.showErrorMessage("Unable to load specification. Error: " + error)
      );
  }

  onSubmit(specification: Specification): void {
    this.loadingService.showSpinner();

    this.specificationService.update(specification)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        specification => this.router.navigate(['/specifications', specification.id]),
        error => this.alertMessageService.showErrorMessage("Unable to update specification. Error: " + error)
      );
  }
}
