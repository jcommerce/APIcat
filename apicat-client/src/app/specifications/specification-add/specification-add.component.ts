import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {AlertMessageService} from "../../shared/alert/alert-message.service";
import {LoadingIndicatorService} from "../../shared/loading-indicator/loading-indicator.service";
import {Specification} from "../../model/specification";
import {SpecificationService} from "../specification.service";

@Component({
  selector: 'specification-add',
  templateUrl: './specification-add.component.html',
  styleUrls: ['./specification-add.component.scss'],

})
export class SpecificationAddComponent {
  specification: Specification = new Specification();

  constructor(private specificationService: SpecificationService,
              private router: Router,
              private alertMessageService: AlertMessageService,
              private loadingService: LoadingIndicatorService) {
  }

  onSubmit(specification: Specification): void {
    this.loadingService.showSpinner();

    this.specificationService.create(specification)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        specification => this.router.navigate(['/specifications', specification.id]),
        error => this.alertMessageService.showErrorMessage("Unable to save specification. Error:" + error)
      );
  }
}
