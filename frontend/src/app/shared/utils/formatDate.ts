import dayjs from 'dayjs';

export const formatTime = (date?: Date | string): string | undefined => {
  if (!date) return undefined;
  return dayjs(date).format('YYYY-MM-DD').toString();
}
