import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.data.data.SearchRecord
import com.example.cherry_pick_android.databinding.ItemSearchBinding

class SearchRecordAdapter(private val record: MutableList<SearchRecord>):
    RecyclerView.Adapter<SearchRecordAdapter.ViewHolder>() {

    // 뷰 홀더 클래스
    class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setRecordItem(records: String) {
            binding.btnRecentSearchItem.text = records
        }

    }

    // 아이템 레이아웃 호출
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    // 호출한 내용으로 bind
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setRecordItem(record[position].record)

        // 삭제 버튼 클릭 이벤트 설정
        viewHolder.binding.ibtnDeleteRecentSearch.setOnClickListener {
            record.removeAt(position) // 아이템 삭제
            notifyItemRemoved(position) // 삭제된 아이템 위치 업데이트
            notifyItemRangeChanged(position, record.size)  // 아이템 사이즈 업데이트
        }
    }

    // 데이터 크기 반환
    override fun getItemCount() = record.size
}