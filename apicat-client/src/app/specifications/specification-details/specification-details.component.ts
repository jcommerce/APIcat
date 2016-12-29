import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {
  ConfirmationModalComponent,
  ModalAction,
  ModalResult
} from "../../shared/confirmation-modal/confirmation-modal.component";
import {AlertMessageService} from "../../shared/alert/alert-message.service";
import {LoadingIndicatorService} from "../../shared/loading-indicator/loading-indicator.service";
import {Specification} from "../../model/specification";
import {SpecificationService} from "../specification.service";

@Component({
  selector: 'specification-details',
  templateUrl: './specification-details.component.html',
  styleUrls: ['./specification-details.component.scss'],

})
export class SpecificationDetailsComponent implements OnInit {
  specification: Specification;
  confirmationModal: ConfirmationModalComponent;

  constructor(private specificationService: SpecificationService,
              private route: ActivatedRoute,
              private router: Router,
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

  delete(specification: Specification): void {
    this.confirmationModal.show(specification);
  }

  onModalClosed(result: ModalResult): void {
    if (result.action === ModalAction.POSITIVE) {
      let specification = result.topic as Specification;
      this.deleteSpecification(specification);
    }
  }

  private deleteSpecification(specification: Specification): void {
    this.loadingService.showSpinner();

    this.specificationService
      .delete(specification.id)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        () => this.router.navigate(['/specifications']),
        error => this.alertMessageService.showErrorMessage("Unable to delete specification. Error: " + error)
      );
  }

  onModalLoaded(modal: ConfirmationModalComponent): void {
    this.confirmationModal = modal;
  }
}
