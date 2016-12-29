import {Component} from "@angular/core";
import {LoadingIndicatorService} from "./loading-indicator.service";

@Component({
  selector: "loading-indicator",
  templateUrl: './loading-indicator.component.html',
  styleUrls: ['./loading-indicator.component.scss'],
})
export class LoadingIndicatorComponent {
  visible: boolean = false;

  constructor(private loadingIndicatorService: LoadingIndicatorService) {
    loadingIndicatorService.visible
      .subscribe(visible => {
        this.visible = visible;
      });
  }
}
