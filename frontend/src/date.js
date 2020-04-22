export function formatDate(date) {
  if (!date) return "-";
  let options = {};
  return new Date(date).toLocaleString("de-CH", options);
}
