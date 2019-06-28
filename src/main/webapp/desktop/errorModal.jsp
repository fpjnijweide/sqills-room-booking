<!-- Modal -->
<div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" style="color: white" id="exampleModalLongTitle">Error</h5>
                <button type="button" class="close" onclick="checkIfFormAndOpen()" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div id="error-text" style="color: white">

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" style="background-color: #FF7212"
                        onclick="checkIfFormAndOpen()" data-dismiss="modal">Close
                </button>
            </div>
        </div>
    </div>
</div>
