import Swal from 'sweetalert2';
import withReactContent from 'sweetalert2-react-content';

const MySwal = withReactContent(Swal);

export const showSuccessAlert = (title, text = '') => {
  MySwal.fire({
    title: <strong>{title}</strong>,
    html: text,
    icon: 'success',
    showClass: {
      popup: 'animate__animated animate__fadeInDownBig'
    },
    hideClass: {
      popup: 'animate__animated animate__zoomOut'
    }
  });
};

export const showWarningAlert = (title, text = '') => {
  MySwal.fire({
    title: <strong>{title}</strong>,
    html: text,
    icon: 'warning',
    showClass: {
      popup: 'animate__animated animate__fadeInDown'
    },
    hideClass: {
      popup: 'animate__animated animate__zoomOut'
    }
  });
};

export const showErrorAlert = (title, text = '') => {
  MySwal.fire({
    title: <strong>{title}</strong>,
    html: text,
    icon: 'error',
    showClass: {
      popup: 'animate__animated animate__shakeX'
    },
    hideClass: {
      popup: 'animate__animated animate__hinge'
    }
  });
};

export const showConfirmAlert = async (title, text = '') => {
  const result = await MySwal.fire({
    title,
    html: text,
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Yes',
    cancelButtonText: 'Cancel',
    reverseButtons: true,
    showClass: {
      popup: 'animate__animated animate__flipInY'
    },
    hideClass: {
      popup: 'animate__animated animate__flipOutY'
    }
  });

  return result.isConfirmed;
};

export const showDataPreviewAlert = async (filteredRows, importType, customTitle = 'Preview Filtered Data!') => {
  if (!filteredRows || filteredRows.length === 0) {
    return false;
  }

  const columnKeys = Object.keys(filteredRows[0] || {});
  const toCapital = key =>
    key
      .replace(/([A-Z])/g, ' $1')
      .replace(/_/g, ' ')
      .replace(/\b\w/g, c => c.toUpperCase());

  const htmlTable = `
    <div style="max-height: 300px; overflow-y: auto; text-align: center;">
      <table style="width: 100%; border-collapse: collapse;">
        <thead>
          <tr style="background: #87CEEB; color: white;">
            ${columnKeys
      .map(key => `<th style="padding: 6px; border: 1px solid #ddd;">${toCapital(key)}</th>`)
      .join('')}
          </tr>
        </thead>
        <tbody>
          ${filteredRows
      .map(
        row => `
            <tr>
              ${columnKeys
            .map(val => {
              const cell = row[val];
              const displayValue = cell === null || cell === undefined || cell === '' ? '-' : cell;
              const width = Math.min(200, Math.max(60, String(displayValue).length * 10));
              return `<td style="padding: 6px; border: 1px solid #ddd; text-align: center; min-width: ${width}px;">${displayValue}</td>`;
            })
            .join('')}
            </tr>
          `
      )
      .join('')}
        </tbody>
      </table>
    </div>
  `;

  const result = await MySwal.fire({
    title: customTitle,
    html: htmlTable,
    icon: 'info',
    confirmButtonText: 'Export to Excel',
    cancelButtonText: 'Cancel',
    showCancelButton: true,
    customClass: {
      popup: 'animate__animated animate__fadeInDownBig',
    },
    showClass: {
      popup: 'animate__animated animate__flipInX',
    },
    hideClass: {
      popup: 'animate__animated animate__flipOutX',
    },
    width: '90%',
  });

  return result.isConfirmed;
};
