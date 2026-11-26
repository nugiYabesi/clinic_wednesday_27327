function LibraryButton({ buttonAction, buttonLabel, style }) {
  return (
    <button type="button" onClick={buttonAction} style={style}>
      {buttonLabel}
    </button>
  )
}

export default LibraryButton
