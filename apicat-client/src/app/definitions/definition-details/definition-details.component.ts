import {Component, OnInit} from "@angular/core";
import {Definition} from "../../model/definition";
import {DefinitionService} from "../definition.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {
  ConfirmationModalComponent,
  ModalAction,
  ModalResult
} from "../../common/confirmation-modal/confirmation-modal.component";
import {AlertMessageService} from "../../common/alert/alert-message.service";
import {LoadingIndicatorService} from "../../common/loading-indicator/loading-indicator.service";

@Component({
  selector: 'definition-details',
  templateUrl: './definition-details.component.html',
  styleUrls: ['./definition-details.component.scss'],

})
export class DefinitionDetailsComponent implements OnInit {
  definition: Definition;
  confirmationModal: ConfirmationModalComponent;

  constructor(private definitionService: DefinitionService,
              private route: ActivatedRoute,
              private router: Router,
              private alertMessageService: AlertMessageService,
              private loadingService: LoadingIndicatorService) {
  }

  ngOnInit(): void {
    this.loadingService.showSpinner();

    this.route.params
      .switchMap((params: Params) =>
        this.definitionService.getDefinition(+params['id']).finally(() => this.loadingService.hideSpinner())
      )
      .subscribe(
        definition => this.definition = definition,
        error => this.alertMessageService.showErrorMessage("Unable to load definition. Error: " + error)
      );
  }

  delete(definition: Definition): void {
    this.confirmationModal.show(definition);
  }

  onModalClosed(result: ModalResult): void {
    if (result.action === ModalAction.POSITIVE) {
      let definition = result.topic as Definition;
      this.deleteDefinition(definition);
    }
  }

  private deleteDefinition(definition: Definition): void {
    this.loadingService.showSpinner();

    this.definitionService
      .delete(definition.id)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        () => this.router.navigate(['/definitions']),
        error => this.alertMessageService.showErrorMessage("Unable to delete definition. Error: " + error)
      );
  }

  onModalLoaded(modal: ConfirmationModalComponent): void {
    this.confirmationModal = modal;
  }
}
