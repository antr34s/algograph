import { StyleSheet, Pressable,Text,View } from 'react-native';
import { Cell as CellType } from '../types/grid';
import { CELL_COLORS } from '../constants/colors';

interface Props {
  cell: CellType;
  size: number;
  onPress: (cell: CellType) => void;
  onPressIn: () => void;
  onPressOut: () => void;
  isPressing: boolean;
}

export default function Cell({
  cell,
  size,
  onPress,
  onPressIn,
  onPressOut,
  isPressing,
}: Props) {
  return (
    <Pressable
      onPressIn={onPressIn}
      onPressOut={onPressOut}
      onPress={() => onPress(cell)}
      onHoverIn={() => {
        if (isPressing) onPress(cell);
      }}
      style={[
        styles.cell,
        {
          backgroundColor: CELL_COLORS[cell.type],
          width: size,
          height: size,
        },
      ]}
    >
      {cell.type === 'obstacle' && cell.weight !== Infinity && (
        <Text selectable={false} style={styles.weightNumber}>{cell.weight}</Text>
      )}
    </Pressable>
  );
}

const styles = StyleSheet.create({
  weightNumber: {
    color: '#000',
    fontSize: 12,
    fontWeight: 'bold',
    textAlign: 'center',
    textShadowColor: '#ff00ff',
    textShadowRadius: 4,
  },
  cell: {
    borderWidth: 0.5,
    borderColor: '#00ffcc',
  },
});