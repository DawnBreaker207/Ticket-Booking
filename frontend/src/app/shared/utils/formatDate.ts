import dayjs from 'dayjs';

export const formatDate = (date?: Date | string): string | undefined => {
  if (!date) return undefined;
  return dayjs(date).format('YYYY-MM-DD').toString();
};
export const formatTime =
  (date?: Date | string): string | undefined => {
    if (!date) return undefined;
    return dayjs(date).format('HH:mm:ss').toString();
  }
